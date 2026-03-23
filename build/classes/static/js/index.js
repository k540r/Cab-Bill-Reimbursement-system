$(document).ready(function() {
	$(function() {
        $(this).bind("contextmenu", function(e) {
            e.preventDefault();
        });
    }); 
	$("#loginB").attr("disabled", true);
	$('.newUser').hide();
	$("#existingUser").click(function() {
		$('.existingUser').show();
		$('.newUser').hide();
		$("#loginB").show();
		$("#registerB").hide();
		$("#forgotB").hide();
	});
	$("#newUser").click(function() {
		$('.existingUser').hide();
		$('.newUser').show();
		$("#loginB").hide();
		$("#registerB").show();
		$("#forgotB").hide();

	});

	$('.verify-idDetails').hide();
	$(".verify-idbtn").click(function() {
		$('.verify-idDetails').show();
	});

	$('.verify-idDetails').hide();
	$(".forgotPass").click(function() {
		$('.forgetPass').show();
		$('.verify-idDetails').hide();
		$('.existingUser').hide();
		$('.yr-btn-group').hide();
		$("#loginB").hide();
		$("#registerB").hide();
		$("#forgotB").show();
		$("#backB").show();
		$("#forgotB").removeClass("btn-success");
		$("#forgotB").removeClass("btn-danger");
		$("#forgotB").addClass("btn-primary");
		$("#forgotB").html("Check User");
		$("#usernameChk").attr("readonly", false);
		$("#usernameChk").val("");
		$("#forgotL").hide();
		$("#forgotS").hide();
	});

	$("#backB").click(function() {
		$('.existingUser').show();
		$('.yr-btn-group').show();
		$('.forgetPass').hide();
		$("#loginB").show();
		$("#closeB").show();
		$("#backB").hide();
		$('.newUser').hide();

	});

	$("#startB").click(function() {
		$('.existingUser').show();
		$('.yr-btn-group').show();
		$('.forgetPass').hide();
		$("#loginB").show();
		$("#closeB").show();
		$("#backB").hide();
		$('.newUser').hide();
		$('.verify-idDetails').hide();
		$("#registerB").hide();
		$("#existingUser").prop("checked", true);



	});

});


/////

$("#forgotB").click(function() {
	var usr = $("#usernameChk").val();
	if (usr != null && usr != '') {
		$.getJSON("checkUser", { username: usr }, function(j) {
			if (j.statusCode == '1') {
				$("#usernameChk").attr("readonly", true);
				$("#forgotB").html("User verified");
				$("#forgotB").removeClass("btn-primary");
				$("#forgotB").removeClass("btn-danger");
				$("#forgotB").addClass("btn-success");
				$("#forgotL").hide();
				var email = j.rows.email;
				$("#forgotS").html("Username sent on registered email - " + email);
				$("#forgotS").show();
			} else {
				$("#forgotL").show();
			}
		});
	} else {
		alert("Enter username");
	}
});

function checkP() {
	var settings = {
		"url": "testPost",
		"method": "POST",
		"timeout": 0,
		"headers": {
			"Content-Type": "application/json"
		},
		"data": "",
	};

	$.ajax(settings).done(function(response) {
		console.log(response);
	});
}

function checkPost() {
	var obj = {
		phone: "8447115740"
	}
	var c = chkV(JSON.stringify(obj));
	var settings = {
		"url": "jsonTest?q=" + c,
		"method": "POST",
		"timeout": 0,
	};
	$.ajax(settings).done(function(j) {
		alert(j);
	});
}

var otp = "";
var encrN = 0;
function userExists() {
	if(encrN == 0){
	var usr = $("#OTPValue").val();
	if (usr != null && usr != '') {
		var c = JSON.stringify({
			username: usr
		});
		var d = chkV(c);
		var settings = {
			"url": "cU?d=" + d,
			"method": "POST",
			"timeout": 0,
		};
		$.ajax(settings).done(function(j) {
			j = setV(j);
			j = JSON.parse(j);
			//alert(j);
			if (j.statusCode == '1') {
					checkUser();			
			} else {
				alert("Invalid username");
			}
		});
	} else {
		alert("Please enter username!");
	}	
	}else{
		checkUser();
	}
	

}

function sendOTP() {
	var usr = $("#OTPValue").val();
	if (usr != null && usr != '') {
		if (encrN == 0) {
			$("#OTPValue").val(chkV(usr));
			$("#OTPValue").prop("type", "password");
			usr = usr;
		} else {
			usr = setV(usr);
		}
		var c = JSON.stringify({
			username: usr
		});
		var d = chkV(c);
		var settings = {
			"url": "getotplogin?d=" + d,
			"method": "POST",
			"timeout": 0,
		};
		$.ajax(settings).done(function(j) {
			j = setV(j);
			j = JSON.parse(j);
			//alert(j);
			if (j.statusCode == '1') {
				//otp = j.rows.password;
				$("#usernameF").val(j.rows.username);
				alert(j.message);
		//		alert("OTP sent on registered mobile number.");
				
				$("#loginB").attr("disabled", true);
				//	$("#sendOTPButton").attr("disabled", true);
				$("#verifyDiv").show();
				//	$("#resendDiv").show();
				$("#OTPValue").attr("readonly", true);
				//phoneF = j.rows.mobile;
				/* alert("username"+j.rows.username);
				alert("password"+j.rows.key); */
				encrN = 1;
			} else {
				alert("Invalid username");
				$("#loginB").attr("disabled", true);
			}
		});
	} else {
		alert("Please enter username!");
	}

}

function checkUser() {
	var usr = $("#OTPValue").val();
	if (usr != null && usr != '') {
		var c = JSON.stringify({
			username: usr
		});
		var d = chkV(c);
		var settings = {
			"url": "otpL?d=" + d,
			"method": "POST",
			"timeout": 0,
		};
		$.ajax(settings).done(function(j) {
			j = setV(j);
			j = JSON.parse(j);
			//alert(j);
			if (j.statusCode == '1' || j.statusCode == '2') {
				//otp = j.rows.password;
				sendOTP();
			} else {
				alert("Please try again after 10 minutes");
			}
		});
	} else {
		alert("Please enter username");
	}
}


function resendOTP() {
	var usr = $("#OTPValue").val();
	usr = setV(usr);
	if (usr != null && usr != '') {
		var c = JSON.stringify({
			phone: usr
		});
		var d = chkV(c);
		var settings = {
			"url": "resendOtp?d=" + d,
			"method": "POST",
			"timeout": 0,
		};
		$.ajax(settings).done(function(j) {
			j = setV(j);
			j = JSON.parse(j);
			if (j.statusCode == '1') {
				//otp = j.rows.password;
				alert("OTP sent on registered mobile number and email.");
				$("#loginB").attr("disabled", false);
				$("#sendOTPButton").attr("disabled", true);
				$("#verifyDiv").show();
				$("#OTPValue").attr("readonly", true);
			} else {
				alert("Invalid username");
				$("#loginB").attr("disabled", true);
			}
		});
	} else {
		alert("Please enter username!");
	}

}


function verifyOTP() {
	var otpV = $("#OTPValueV").val();
	var usr = $("#OTPValue").val();
	if (otpV != null && otpV != '') {
		$("#OTPValueV").val(chkV(otpV));
		usr = setV(usr);
		var c = JSON.stringify({
			phone: usr,
			otp: otpV
		});
		var d = chkV(c);
		var settings = {
			"url": "verifyOTP?d=" + d,
			"method": "POST",
			"timeout": 0,
		};
		$.ajax(settings).done(function(j) {
			j = setV(j);
			j = JSON.parse(j);
			if (j.statusCode == '1') {
				alert("OTP verification successful. Login to continue.");
				$("#loginB").attr("disabled", false);
				$("#sendOTPButton").attr("disabled", true);
				$("#verifyOTPButton").attr("disabled", true);
				$("#resendOTPButton").attr("disabled", true);
				$("#passwordF").val(otpV);
				$("#OTPValueV").attr("readonly", true);
				$("#resendOTPButton").hide();
			} else if (j.statusCode == '2') {
				alert("OTP expired. Please generate new OTP.");
				$("#loginB").attr("disabled", false);
				$("#sendOTPButton").attr("disabled", false);
				$("#verifyDiv").hide();
				$("#resendDiv").hide();
			} else {
				alert("Invalid OTP");
				$("#loginB").attr("disabled", true);
				$("#sendOTPButton").attr("disabled", true);
			}
		});
	} else {
		alert("Please enter OTP!");
	}

}

function submitForm() {
	//var un = setV($("#usernameF").val());
	var up = chkV($("#passwordF").val());
	$("#passwordF").val(up);
	
	//var d = chkV();
	//alert(up);
	//alert($("#usernameF").val());
	$("#loginF").submit();
}

function registerForm() {
	var selected = $('input[name="userTypeR"]:checked').val();
	//var selected = $('input[name="userTypeO"]:checked').val();

	$("#usertypeV").val(selected);
	$("#registerF").submit();

}


$("#sendOTPButton").click(function(){
	userExists();
});

$("#verifyOTPButton").click(function(){
	verifyOTP();
});

$("#loginB").click(function(){
	submitForm();
});

$("#registerB").click(function(){
	registerForm();
});


function userTypeOption(usr) {
	var ut = usr.value;
	if (ut == 'individual') {
		$("#usernameChk").attr("placeholder", "Enter your Aadhaar/PAN/MCA number");
	} else {
		$("#usernameChk").attr("placeholder", "Enter your PAN/MCA number");
	}
}

$('#OTPValue').keyup(function() {
	var result = document.getElementById("OTPValue").value;
	result = result.replace(/[<> )(]/g,'').replace(/[\[\]']+/g,'').replace(/[{}]/g, "");
	$("#OTPValue").val(result);

if( ValidateEmail() || mobileNumberError()) {
    $("#sendOTPButton").attr("disabled", false);
}else {
    $("#sendOTPButton").attr("disabled", true);
}
});


function ValidateEmail() {
    var email = document.getElementById("OTPValue").value;
    var expr = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    if (!expr.test(email)) {
        $("#sendOTPButton").attr("disabled", true);
        return false;
    }
    else{
    	$("#sendOTPButton").attr("disabled", false);
        return true;
    }
}

function mobileNumberError() {
    var email = document.getElementById("OTPValue").value;
    var expr = /^([6789][0-9]{9})$/;
    if (!expr.test(email)) {
        $("#sendOTP").attr("disabled", true);
        return false;
    }
    else{
    	$("#sendOTP").attr("disabled", false);
    	return true;
    }
}


window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', 'G-60SD45302K');

