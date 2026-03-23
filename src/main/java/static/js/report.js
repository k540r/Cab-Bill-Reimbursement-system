$(document).ready(function() {
	dataDetail();
	});
function dataDetail(){
    $('#userTable').DataTable({
    	responsive: true,
    	dom:'Bfrtip',
    	"ordering": false,
    	language: {
   		    "infoFiltered": "",
   		},
   		buttons: [
   			'copy',
         {
             extend: 'excel',
             title:'User List'
         },
         {
        	 title:'User List',
        	 extend: 'pdf',
             orientation: 'potrait',
             pageSize: 'A4'
         },
   		]
    });
}