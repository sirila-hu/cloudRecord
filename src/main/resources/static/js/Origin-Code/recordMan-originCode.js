			$(".record-item").hover(function(){
				$(this).children(".btn-close").fadeIn();
				$(this).children(".btn-fix").fadeIn();
			}, function(){
				$(this).children(".btn-close").fadeOut();
				$(this).children(".btn-fix").fadeOut();
			});
				
			$(".btn-fix").click(function(){
					var a = $(this).siblings("a");
					var r_id = a.children(".r_id").text();
					var r_name= a.children(".r_name").text();
					$("#fix_r_name").val(r_name);
					$("#r_id").val(r_id);
					$('#fixRecord-form').modal("show");
				});

			$(".btn-close").click(function(){
				var r_id = $(this).parents(".record-item").find(".r_id").text();
				var page = /*[[${page}]]*/ 1;
				console.log(r_id);
				layer.confirm('确定删除？', {icon: 3, title:'确认'}, function(index){
					location.replace("recordMan/delRecord?r_id=" + r_id +"&page=" + page);
					layer.close(index);
				});
			});