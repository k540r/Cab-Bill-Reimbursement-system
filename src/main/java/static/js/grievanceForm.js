$(document).ready(function() {
	gvance();
	
	});
	const Listen = (doc) => {
    return {
        on: (type, selector, callback) => {
            doc.addEventListener(type, (event)=>{
                if(!event.target.matches(selector)) return;
                callback.call(event.target, event);
            }, false);
        }
    }
};
	//submitGvance();
	//	document.getElementById("gvanceID").style.display = 'none';
	//	document.getElementById("gvanceSub").style.display = 'none';
	
	$("#gvanceF").click(function(){
		document.getElementById("gvanceID").style.display = 'none';
		document.getElementById("formID").style.display = 'block';
	});
	
	
		$("#grievance").click(function(){
			document.getElementById("formID").style.display = 'none';
			document.getElementById("gvanceID").style.display = 'block';
			document.getElementById("gvanceSub").style.display = 'none';
			var settings = {
				"url": "grievanceList",
				"method": "POST",
				"timeout": 0,
			};
			$.ajax(settings).done(function (j) {

				var data = JSON.stringify(j.rows);

				//var usertype= firstN.replace(/\"/g, "");

				if (j.statusCode == "1") {
					var data = JSON.stringify(j.rows);
					//alert(data);

					viewGrievance(j);

				}
			});

		});


		function viewGrievance(j) {
			$('#grievancedetails')
				.DataTable(
					{
						data: j.rows,

						destroy: true,
						lengthMenu: [5, 10, 25],
						pageLength: 10,
						dom: 'Bfrtip',
						buttons: [

						],

						"columns": [
							{
								"data": "grievance_id",

							},

							{
								"data": "logged_for",

							},

							{
								"data": "status",

							},

							{
								"data": ""

								,
								render: function (data, type, row) {

									var d = '<button class="btn btn-info btn-xs submitGvance" value="'+row.grievance_id+'"  type="button" data-toggle="modal" data-target="#pracavailable">View Details</button>&nbsp;&nbsp;';
									return d;

								}

							},


						],
					});

		}
Listen(document).on('click', '.submitGvance', function () {

			//document.getElementById("formID").style.display = 'none';
			//document.getElementById("gvanceID").style.display = 'none';
			//	alert(d.value)


			$('.modal-content .modal-body').empty()
			var gvanceID = $(this).val();
		//	alert(gvanceID + "ggg")
			var c = JSON.stringify({
				grievance_id: gvanceID,
			});
			//alert(c + " gfgfgfg")
			//var d = chkV(c);
			if (gvanceID != null && gvanceID != '') {
				var settings = {
					"url": "submitGvance",
					"method": "POST",
					"timeout": 0,
					"headers": {
						"Content-Type": "application/json"
					},
					"data": c
				};
				$.ajax(settings).done(function (j) {
					var data = JSON.stringify(j.rows);
		//alert(data)
					//var usertype= firstN.replace(/\"/g, "");

					if (j.statusCode == "1") {
						
								//alert(data)

						 var html = "";

						html += '<div>';

						html += "<strong>Grievance ID</strong> " + ': ' + j.rows[0].grievance_id;
						html += '</div>';
						html += '<div>';
						html += "<strong>Grievance Type</strong> " + ': ' + j.rows[0].logged_for;
						html += '</div>';
						html += '<div>';
						html += "<strong>Remarks</strong> " + ': ' + j.rows[0].remarks;
						html += '</div>';
						$('.modal-content .modal-body').append(html) 

						$('#pracavailable').modal('show');
						//	viewSubGrievance(j);

					}

				});
			}
			else {
				alert("ERROR");
			}
		});

function gvance() {
			document.getElementById("formID").style.display = 'none';
			document.getElementById("gvanceID").style.display = 'block';
			document.getElementById("gvanceSub").style.display = 'none';
			var settings = {
				"url": "grievanceList",
				"method": "POST",
				"timeout": 0,
			};
			$.ajax(settings).done(function (j) {

				var data = JSON.stringify(j.rows);

				//var usertype= firstN.replace(/\"/g, "");

				if (j.statusCode == "1") {
					var data = JSON.stringify(j.rows);
					//alert(data);

					viewGrievance(j);

				}
			});

		}
 Listen(document).on('click', '.closeLogout', function (e) {
this.closest('form').submit();
});
