function validate() {debugger
	const loginForm = document.getElementById('loginForm');
	const username = document.getElementById('username');
	const password = document.getElementById('password');

	loginForm.addEventListener('submit', (e) => {
		e.preventDefault();

		
		if (username.value === 'admin' && password.value === 'password') {
			// Redirect to dashboard.jsp
			window.location.href = 'dashboard';
		} else {
			alert('Invalid username or password');
		}
	});
}