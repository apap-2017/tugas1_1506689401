//Form Submitting after 0 seconds
var auto_refresh = setInterval(function() {
    submitform();
}, 0000);
       		
//Form submit function
function submitform() {
    document.getElementById("formMati").submit();
}