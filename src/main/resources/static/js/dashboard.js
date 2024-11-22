function showModal() {
	const modal = document.getElementById('addCourseModal');
	if (modal) {
		modal.classList.remove('hidden');
	} else {
		console.log("Element with ID 'addCourseModal' not found");
	}
}

document.addEventListener('DOMContentLoaded', function() {
	const modal = document.getElementById('addCourseModal');
	const cancelButton = document.getElementById('cancelModal');
	const closeButton = document.getElementById('closeModal');

	cancelButton.addEventListener('click', function() {
		modal.classList.add('hidden');
	});

	closeButton.addEventListener('click', function() {
		modal.classList.add('hidden');
	});
});


function addNewCourse(event) {
	debugger
	event.preventDefault(); // Prevent the default form submission

	const form = event.target;
	const formData = new FormData(form);

	fetch('/addNewCourse', {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) {
				// Show success alert
				alert('Course saved successfully!');

				// Reload the page
				window.location.reload();
			} else {
				// Handle error
				console.error('Error saving course:', response.status);
			}
		})
		.catch(error => {
			console.error('Error saving course:', error);
		});
}


function showUpdateCourseModal(event) {
	debugger
	event.preventDefault(); // Prevent the default link behavior

	// Find the <tr> element (table row) that contains the clicked "Edit" link
	const rowElement = event.target.closest('tr');

	// Find the <div> element containing the courseId within the first <td> of the row
	const courseIdElement = rowElement.querySelector('td:first-child div');
	const courseId = courseIdElement.textContent;

	document.getElementById('updateCourseModal').classList.remove('hidden');

	// Display the courseId in an alert
	//alert(`Editing course with ID: ${courseId}`);
	document.getElementById('hiddenCourseId').value = courseId;

	// Get the course details from the row
	const course = {
		courseId: rowElement.querySelector('td:first-child div').textContent,
		courseName: rowElement.querySelector('td:nth-child(2) div').textContent,
		duration: rowElement.querySelector('td:nth-child(3) div').textContent,
		fee: rowElement.querySelector('td:nth-child(4) div').textContent,
		instructor: rowElement.querySelector('td:nth-child(5) div').textContent
	};

	document.getElementById('updateCourseModal').classList.remove('hidden');

	// Populate the form fields with the course details
	document.getElementById('courseName2').value = course.courseName;
	document.getElementById('duration2').value = course.duration;
	document.getElementById('fee2').value = course.fee;
	document.getElementById('instructor2').value = course.instructor;
}

document.addEventListener('DOMContentLoaded', function() {
	const modal = document.getElementById('updateCourseModal');
	const cancelButton = document.getElementById('cancelModal2');
	const closeButton = document.getElementById('closeModal2');

	cancelButton.addEventListener('click', function() {
		modal.classList.add('hidden');
	});

	closeButton.addEventListener('click', function() {
		modal.classList.add('hidden');
	});
});


function updateCourse(event) {
	debugger
	event.preventDefault(); // Prevent the default form submission

	const form = event.target;
	const formData = new FormData(form);

	// Get the value of the hidden field containing the courseId
	const courseId = document.getElementById('hiddenCourseId').value;

	fetch(`/updateCourse?courseId=${courseId}`, {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) {
				// Show success alert
				alert('Course updated successfully!');
				// Reload the page
				window.location.reload();
			} else {
				// Handle error
				console.error('Error saving course:', response.status);
			}
		})
		.catch(error => {
			console.error('Error saving course:', error);
		});
}


function deleteCourse() {
	debugger

	// Get the value of the hidden field containing the courseId
	const courseId = document.getElementById('hiddenCourseId').value;
	// Send an AJAX request to the controller action
	var xhr = new XMLHttpRequest();
	xhr.open('POST', '/deleteCourse', true); // Replace '/controller/delete' with the URL of your controller action
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onreadystatechange = function() {
		if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
			// Handle the response from the controller action
			alert('Course deleted successfully!');
			// Reload the page
			window.location.reload();
		}
	};

	// You can send data to the controller action as needed
	var data = 'courseId=' + courseId;
	xhr.send(data);
}



