function showModal() {
	const modal = document.getElementById('addInstrctorModal');
	if (modal) {
		modal.classList.remove('hidden');
	} else {
		console.log("Element with ID 'addCourseModal' not found");
	}
}

document.addEventListener('DOMContentLoaded', function() {
	const modal = document.getElementById('addInstrctorModal');
	const cancelButton = document.getElementById('cancelModal');
	const closeButton = document.getElementById('closeModal');

	cancelButton.addEventListener('click', function() {
		modal.classList.add('hidden');
	});

	closeButton.addEventListener('click', function() {
		modal.classList.add('hidden');
	});
});

function addNewInstructor(event) {
	debugger
	event.preventDefault(); // Prevent the default form submission

	const form = event.target;
	const formData = new FormData(form);

	fetch('/addNewInstructor', {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) {
				// Show success alert
				alert('Instructor created successfully!');

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