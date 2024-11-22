package com.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
//@Controller
@RestController
public class HomeController {
	@Autowired
	MasterCourseRepository masterCourseRepository;
	@Autowired
	UserDetailsRepo userDetailsRepo;
	@Autowired
	InstructorRepo instructorRepo;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("message", "Hello from Thymeleaf!");
		return "home";
	}

	@GetMapping("/dashboard")
	public String getDashboard(Model model) {
		List<MasterCourse> masterCoursesList = masterCourseRepository.findAll();
		model.addAttribute("masterCourcesList", masterCoursesList);
		return "dashboard";
	}

	@GetMapping("/instructors")
	public String getInstructors(Model model) {
		List<Instructor> instructorList = instructorRepo.findAll();
		if (!instructorList.isEmpty()) {
			model.addAttribute("instructorsList", instructorList);
		}
		return "instructors";
	}

	@PostMapping("/loginControleHandler")
	public String loginBusinessLogic(@RequestParam(name = "username") String userName,
			@RequestParam(name = "password") String password, HttpSession session, Model model) {
		Optional<UserDetails> userDetails = userDetailsRepo.findByUsername(userName);

		if (userDetails.isPresent()) {
			UserDetails singleUserDetails = userDetails.get();
			String dBUserName = singleUserDetails.getUserName();
			String dBPassword = singleUserDetails.getPassword();
			String userGrade = singleUserDetails.getUserGrade();

			if (!userGrade.equals("1")) {
				// Un-Authorized user
				model.addAttribute("errorMessage", "Un-Authorized User..!");
				return "home"; // Return the current view name
			}
			if (!password.equals(dBPassword)) {
				// Invalid password
				model.addAttribute("errorMessage", "Invalid password..!");
				return "home"; // Return the current view name
			}
			if (userGrade.equals("1") && userName.equals(dBUserName) && password.equals(dBPassword)) {
				List<MasterCourse> masterCoursesList = masterCourseRepository.findAll();
				model.addAttribute("masterCourcesList", masterCoursesList);
				model.addAttribute("sessionedUserName", userName);
				return "dashboard";
			}

		}
		model.addAttribute("errorMessage", "Un-Authorized User..!");
		return "home";
	}

	@PostMapping("/addNewCourse")
	public ResponseEntity<?> addNewCourse(@RequestParam("courseName") String courseName,
			@RequestParam("duration") String duration, @RequestParam("fee") String fee,
			@RequestParam("instructor") String instructor) {

		MasterCourse masterCourse = new MasterCourse();
		masterCourse.setCourseName(courseName);
		masterCourse.setDuration(duration);
		masterCourse.setFee(Double.parseDouble(fee));
		masterCourse.setInstructor(instructor);
		masterCourse.setCreatedDate(Utillities.getCurrentDate());
		masterCourse.setCreatedTime(Utillities.getCurrentTime());

		masterCourseRepository.save(masterCourse);

		Map<String, String> response = new HashMap<>();
		response.put("message", "Course saved successfully");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/updateCourse")
	public ResponseEntity<?> updateCourse(@RequestParam("courseName2") String courseName,
			@RequestParam("duration2") String duration, @RequestParam("fee2") String fee,
			@RequestParam("instructor2") String instructor, @RequestParam("courseId") int courseId) {

		Optional<MasterCourse> existingMasterCourse = masterCourseRepository.findById(courseId);
		if (existingMasterCourse.isPresent()) {
			MasterCourse singleMasterCourse = existingMasterCourse.get();
			singleMasterCourse.setCourseName(courseName);
			singleMasterCourse.setDuration(duration);
			singleMasterCourse.setFee(Double.parseDouble(fee));
			singleMasterCourse.setInstructor(instructor);
			singleMasterCourse.setCreatedDate(Utillities.getCurrentDate());
			singleMasterCourse.setCreatedTime(Utillities.getCurrentTime());
			masterCourseRepository.save(singleMasterCourse);
		}

		Map<String, String> response = new HashMap<>();
		response.put("message", "Course updated successfully");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/deleteCourse")
	public ResponseEntity<?> deleteCourse(@RequestParam(name = "courseId", required = false) Integer courseId) {
		Optional<MasterCourse> existingMasterCourse = masterCourseRepository.findById(courseId);
		if (existingMasterCourse.isPresent()) {
			MasterCourse singleMasterCourse = existingMasterCourse.get();
			masterCourseRepository.delete(singleMasterCourse);
		}

		Map<String, String> response = new HashMap<>();
		response.put("message", "Course deleted successfully");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/addNewInstructor")
	public ResponseEntity<?> addNewInstructor(@RequestParam(name = "name", required = false) String name,
			@RequestParam("emailID") String emailID, @RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("subjects") String subjects) {

		Instructor instructor = new Instructor();
		instructor.setEmailAddress(emailID);
		instructor.setName(name);
		instructor.setPhoneNumber(phoneNumber);
		instructor.setSubjectHandling(subjects);

		instructorRepo.save(instructor);

		Map<String, String> response = new HashMap<>();
		response.put("message", "Instructor created successfully!");
		return ResponseEntity.ok().build();
	}

	@RequestMapping(path = "/getStudentDetails", method = RequestMethod.POST)
	public String getStudentsView(Model model,
			@RequestParam(name = "userGradeUnique", required = false) String userGradeUnique) {
		List<UserDetails> userDetailsList;
		userGradeUnique = userGradeUnique == null ? "2" : userGradeUnique; // Default to grade "2" if userGrade is null
		System.out.println("userGradeUnique= " + userGradeUnique);
		userDetailsList = userDetailsRepo.findByUserGrade(userGradeUnique);
		model.addAttribute("studentsList", userDetailsList);
		return "students";
	}

	/*
	 * @GetMapping("/students") public String getStudents(Model
	 * model, @RequestParam(value = "userGrade", defaultValue = "2") String
	 * userGrade) { List<UserDetails> studentsList =
	 * userDetailsRepo.findByUserGrade(userGrade);
	 * model.addAttribute("studentsList", studentsList); return "students"; //
	 * Return the table body fragment }
	 */
	@GetMapping("/students")
	public String getStudents(@RequestParam(name = "selectedFilterOp", required = false) String selectedFilterOp,
			Model model) {
		List<UserDetails> data = userDetailsRepo.findAll();
		model.addAttribute("studentsList", data);
		return "students";
	}

	@GetMapping("/grid")
	public String getGrid(@RequestParam("selectedFilterOp") String selectedFilter, Model model) {
		List<UserDetails> data = userDetailsRepo.findByUserGrade(selectedFilter);
		model.addAttribute("studentsList", data);
		return "students"; // Thymeleaf template name
	}

	// REACT-JS API's REQUESTS HANDLER METHODS.....

	@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
	@RequestMapping(path = "/courses", method = RequestMethod.GET)
	public ResponseEntity<List<MasterCourse>> getCourses() {
		try {
			List<MasterCourse> masterCourseList = masterCourseRepository.findAll();
			return ResponseEntity.ok(masterCourseList); // Correct usage of ResponseEntity
		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
	@RequestMapping(path = "/addNewCourseForReact", method = RequestMethod.POST)
	public ResponseEntity<?> addNewCourseNew(@RequestParam(name = "courseName", required = false) String courseName,
			@RequestParam(name = "duration", required = false) String duration,
			@RequestParam(name = "fee", required = false) String fee,
			@RequestParam(name = "instructor", required = false) String instructor) {

		MasterCourse masterCourse = new MasterCourse();
		masterCourse.setCourseName(courseName);
		masterCourse.setDuration(duration);
		masterCourse.setFee(Double.parseDouble(fee));
		masterCourse.setInstructor(instructor);
		masterCourse.setCreatedDate(Utillities.getCurrentDate());
		masterCourse.setCreatedTime(Utillities.getCurrentTime());

		masterCourseRepository.save(masterCourse);

		Map<String, String> response = new HashMap<>();
		response.put("message", "Course saved successfully");
		return ResponseEntity.ok().build();
	}

	@GetMapping("/instructorsforreact")
	public ResponseEntity<List<Instructor>> getInstructorsnew(Model model) {
		List<Instructor> lst = instructorRepo.findAll();
		return ResponseEntity.ok(lst);
	}

	@GetMapping("/studentsNew")
	public ResponseEntity<List<UserDetails>> getStudentsNew(
			@RequestParam(name = "selectedFilterOp", required = false) String selectedFilterOp, Model model) {
		List<UserDetails> data = userDetailsRepo.findAll();
		return ResponseEntity.ok(data);
	}

	@PostMapping("/removeInstructor")
	public ResponseEntity removeInstructor(@RequestParam("instructorID") long instructorID) {
		instructorRepo.deleteById(instructorID);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/updateInstrctorInfo")
	public ResponseEntity updateInstrctorDetails(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("id") String id, @RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("subjectHandling") String subjectHandling) {
		Optional<Instructor> opInstrctor = instructorRepo.findById(Long.valueOf(id));
		if (opInstrctor.isPresent()) {
			Instructor instructor = opInstrctor.get();
			instructor.setName(name);
			instructor.setEmailAddress(email);
			instructor.setPhoneNumber(phoneNumber);
			instructor.setSubjectHandling(subjectHandling);
			instructorRepo.save(instructor);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/deleteUser")
	public ResponseEntity deleteUser(@RequestParam("studentId") int id) {
		userDetailsRepo.deleteById(Integer.valueOf(id));
		return ResponseEntity.ok().build();
	}

	@PostMapping("/addAdmin")
	public ResponseEntity addAdmin(@RequestParam("email") String email, @RequestParam("message") String message,
			@RequestParam("mobileNum") String mobileNum, @RequestParam("name") String name,
			@RequestParam("userName") String userName, @RequestParam("password") String password,
			@RequestParam("whatsAppNum") String whatsAppNum) {
		try {
			UserDetails userDetails = new UserDetails();
			userDetails.setCreatedDate(Utillities.getCurrentDate());
			userDetails.setCreatedTime(Utillities.getCurrentTime());
			userDetails.setEmail(email);
			userDetails.setMessage(message);
			userDetails.setMobileNum(mobileNum);
			userDetails.setName(name);
			userDetails.setPassword(password);
			userDetails.setUserGrade("1");
			userDetails.setWhatsAppNum(whatsAppNum);
			userDetails.setUserName(userName);
			userDetailsRepo.save(userDetails);
		} catch (Exception e) {
			ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginLogicForReactAdminPortal(@RequestParam("username") String userName,
			@RequestParam("password") String password) {
		Optional<UserDetails> opUserName = userDetailsRepo.findByUsername(userName);
		if (opUserName.isPresent()) {
			UserDetails userDetails = opUserName.get();
			String dbPassword = userDetails.getPassword();
			if (userDetails != null && password.equals(dbPassword)) {
				return ResponseEntity.ok("Login Successfull");
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credintials..!");
	}
	
	

}
