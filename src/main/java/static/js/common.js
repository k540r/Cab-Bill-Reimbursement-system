document.addEventListener("DOMContentLoaded", function() {
  // Select all list items with the class 'nav'
  const navItems = document.querySelectorAll('.nav li');

  // Function to update the active class based on the current URL
  function updateActiveClass() {
    const currentPath = window.location.pathname;
    console.log("Current Path:", currentPath); // Debugging line

    navItems.forEach(item => {
      const link = item.querySelector('a');
      if (link) {
        const href = link.getAttribute('href');
        // Normalize paths for comparison
        const normalizedHref = href.split('/').pop();
        const normalizedCurrentPath = currentPath.split('/').pop();
 console.log("Current Path:", normalizedHref,  normalizedCurrentPath);
        if (normalizedHref === normalizedCurrentPath) {
//			var title=item.getAttribute('data-target');
//			document.getElementsByTagName('title')[0].textContent=title;
          item.classList.add('active');
        } else {
          item.classList.remove('active');
        }
      }
    });
  }

  // Initial call to set the active class on page load
  updateActiveClass();

  // Add click event listeners to update the active class and navigate
  navItems.forEach(item => {
    const link = item.querySelector('a');
    if (link && link.id !== 'logoutF') {
      item.addEventListener('click', function(event) {
        // Prevent the default action
        event.preventDefault();

        // Remove 'active' class from all items
        navItems.forEach(nav => nav.classList.remove('active'));

        // Add 'active' class to the clicked item
        this.classList.add('active');

        // Redirect to the href of the anchor tag
        window.location.href = link.getAttribute('href');
      });
    }
  });

document.getElementById('logoutF').onclick=function() { 
    		document.getElementById('logoutForm').submit();
    	};
 
});
function logout() {
        // Get CSRF token and header name
        var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        var csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
        
        // Create a new AJAX request
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/logout", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.setRequestHeader(csrfHeader, csrfToken);
        
        // Send the request
        xhr.send();
    }

 function calculateAge(dateOfBirth) {
    const dob = new Date(dateOfBirth);
    const today = new Date();
    
    let years = today.getFullYear() - dob.getFullYear();
    let months = today.getMonth() - dob.getMonth();
    
    // Adjust if the birth month has not occurred yet this year
    if (months < 0 || (months === 0 && today.getDate() < dob.getDate())) {
        years--;
        months += 12;
    }
    
    // Adjust if the birth date has not occurred yet this month
    if (today.getDate() < dob.getDate()) {
        months--;
        if (months < 0) {
            years--;
            months = 11;
        }
    }
    
    return `${years} yrs ${months} months`;
}
    