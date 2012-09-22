
function getTags(callback){
    $.ajax({
     url: "/know/rest/taxonomy/"
}).done(callback);

}


function search(query, callback){
    $.ajax({
        url: "/know/rest/search/?q="+query
    }).done(callback);

}