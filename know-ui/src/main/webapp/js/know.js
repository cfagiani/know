
function getTags(callback){
    $.ajax({
     url: "/know/rest/taxonomy/"
}).done(callback);

}