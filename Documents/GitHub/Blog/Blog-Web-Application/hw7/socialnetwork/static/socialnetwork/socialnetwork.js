
function displayError(message) {
    $("#error").html(message);
}

function getCSRFToken() {
    var cookies = document.cookie.split(";");
    for (var i = 0; i < cookies.length; i++) {
        c = cookies[i].trim();
        if (c.startsWith("csrftoken=")) {
            return c.substring("csrftoken=".length, c.length);
        }
    }
    return "unknown";
}


function sanitize(s) {
    // Be sure to replace ampersand first
    return s.replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
}


function updateLists(){
    var isotime = new Date(Date.now()-5000).toISOString();
    if(window.location.pathname == '/socialnetwork/globalstream/'){
        $.ajax({
            url: "/socialnetwork/refresh-global?last_refresh="+isotime, 
            type : "GET",
            dataType : "json",
            success: updateStream
        });
        
    }
    if(window.location.pathname == '/socialnetwork/followerstream/'){
        $.ajax({
            url: "/socialnetwork/refresh-follower?last_refresh="+isotime, 
            type : "GET", 
            dataType : "json",
            success: updateStream
        });
    }
    
}

function updateStream(contents){
    var comments = contents['comments'];
//     var comments = JSON.parse(contents['comments']);
//    var posts = JSON.parse(contents['posts']);
    var posts = contents['posts'];

    var postauthors;
    var commentauthors;
    var authorIDs;
    var postIDs;
    if(typeof contents['postauthors']!="undefined"&&contents['postauthors']!=""){
//        postauthors = JSON.parse(contents['postauthors']);
          postauthors = contents['postauthors'];

    }
    if(typeof contents['commentauthors']!="undefined"&&contents['commentauthors']!=""){
//        commentauthors = JSON.parse(contents['commentauthors']);
        commentauthors = contents['commentauthors'];
    }
    if(typeof contents['authorIDs']!="undefined"&&contents['authorIDs']!=""){
        authorIDs = contents['authorIDs'];
    }
    if(typeof contents['postIDs']!="undefined"&&contents['postIDs']!=""){
        postIDs = contents['postIDs'];

    }
    var firstID = $("p[id^='post_']").get(0).id;
    for(var i = 0;i<posts.length;i++){
        var post = posts[i];
        $('#'+firstID).prepend( 
            "Post by <a href='/socialnetwork/publicprofile/" +postIDs[i]+"'>"
            +postauthors[i]+"<br/></a> -- "+"<span id='id_post_text_"+post.id+"'>"+post.content+"</span> -- <span id='id_comment_date_time_"+
                post.id+"'>"+post.date_posted+"</span></p>"+ "<div class='input-group input-group-sm'>"
            
            +"<input type='text' id='id_comment_text_input_"+post.id+"'>"
                
            +"<button class='input-group-addon' id='id_comment_button_"+post.id+"' class='btn btn-default' onclick=addComment()>Comment</button></div><br>" )
    }
    var $inputs = $("input[id^='id_comment_text_input']");
    var id;
    for(var i = 0;i<comments.length;i++){
        var comment = comments[i];
        $.each($inputs, function( index ) {
        $input = $inputs.get(index);
        if($input.id.substring(22)==postIDs[i]){
            var date = new Date(comment.date_commented);
            id = $input.id.substring(22);
            $post = $('#post_'+id);
            $post.append("<li id='comment_"+comment.id+"'>Comment by <a href=\'/socialnetwork/publicprofile/"+authorIDs[i]+"'>" +commentauthors[i]+"<br/></a> -- "+"<span id='id_comment_text_"+comment.id+"'>"+comment.content+"</span> -- <span id='id_comment_date_time_"+
                comment.id+"'>"+date+"</span></li>" )
        
        }  
        
    });
    } 
}

function addComment(){
//    $(document).on("click", ".comment_button", function(){
    var comment_text;
    var comment_object;
    var id;
    var $post;
    var $inputs = $("input[id^='id_comment_text_input']");
    $.each($inputs, function( index ) {
       $input = $inputs.get(index);
       if($input.value!=""){
            id = $input.id.substring(22);
            comment_text = $input.value;
            comment_object = $input;
            $post = $('#post_'+id);
        }  
        
    });
    
    
  //  comment_object.val('');
    displayError('');
    $.ajax({
        url: "/socialnetwork/add-comment/"+id,
        type: "POST",
        data: "comment="+comment_text+"&csrfmiddlewaretoken="+getCSRFToken(),
        dataType : "json",
        success: function(comment) {
                var date = new Date(comment.date_commented);
                $post.append("<li id='comment_"+comment.id+"'>Comment by <a href=\'/socialnetwork/publicprofile/"+comment.author_id+"'>" +comment.first_name+" "+comment.last_name+"<br/></a> -- "+"<span id='id_comment_text_"+comment.id+"'>"+comment.content+"</span> -- <span id='id_comment_date_time_"+
                comment.id+"'>"+date+"</span></li>")
        
        }
    });
//    });
    
}
window.load = updateLists;
window.setInterval(updateLists, 5000);
