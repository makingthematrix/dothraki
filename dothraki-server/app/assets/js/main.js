 $(document).ready(function() {

    // process the form
    $('form').submit(function(event) {

        // get the form data
        // there are many ways to get this data using jQuery (you can use the class or id also)
        var formData = {
            'word'              : $('input[name=word]').val()
        };

        $('input[name=word]').val("");
               // process the form
        $.ajax({
            type        : 'POST', // define the type of HTTP verb we want to use (POST for our form)
            url         : '/translate', // the url where we want to POST
            data        : formData, // our data object
            dataType    : 'json', // what type of data do we expect back from the server
            encode      : true
        }).done(function(data) {
              alert(data);
            });

        // stop the form from submitting the normal way and refreshing the page
        event.preventDefault();
    });

});