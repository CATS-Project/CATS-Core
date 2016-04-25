jQuery(document).ready(
	function() {
		var currentId;
		var currentTr;
		$(".del_button").click(
			function(e) {
				e.returnValue = false;
				
				var clickedID = this.id;
				var myData = 'idu=' + clickedID; //build a post data structure   
				var $tr = $(this).closest('tr'); //here we hold a reference to the clicked tr which will be later used to delete the row
				
				console.log('d1 '+myData);				
				currentId = myData;
				currentTr = $tr;
				
				$('#modal1').openModal();
				
				e.preventDefault();
			});
		
		$(".accept_delete").click(
				function(e) {
					e.returnValue = false;
					var myData = currentId;
					var $tr = currentTr;
					console.log('d2 '+myData);
					$('#modal1').closeModal();
				
					$.ajax({
						type : "POST", // HTTP method POST or GET
						url : "deleteUser", //Where to make Ajax calls
						dataType : "text", // Data type, HTML, json etc.
						data : myData, //Form variables
						success : function(response) {
							$tr.children('td, th').css('color','#fff');
							$tr.children('td, th').css('background-color','#e5412d');
							$tr.find('td').fadeOut(1600,function() {
												$tr.remove();
											});
						},
						error : function(xhr,ajaxOptions,thrownError) {
							alert(thrownError);
						}
					});
					
					e.preventDefault();
				});
		
		$(".cancel_delete").click(
				function(e) {
					
					$('#modal1').closeModal();
					e.preventDefault();
				});
	}
);