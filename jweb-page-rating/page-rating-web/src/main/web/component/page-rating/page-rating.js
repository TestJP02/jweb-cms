$(function() {
    $(".rating").each(function(index, starContainer) {
        var stars = $(starContainer).find(".rating__star");

        function restoreRating() {
            stars.removeClass("rating__star--rated");
            for (var i = 0; i < $(starContainer).data("rating"); i += 1) {
                stars.eq(i).addClass("rating__star--rated");
            }
        }

        restoreRating();

        $(starContainer).hover(null, function() {
            restoreRating();
        });

        stars.hover(function(event) {
            stars.removeClass("rating__star--rated");
            for (var i = 0; i < $(event.currentTarget).index() + 1; i += 1) {
                stars.eq(i).addClass("rating__star--rated");
            }
        });

        stars.click(function(event) {
            var score = $(event.currentTarget).index() + 1;
            $(starContainer).addClass("loading");
            $.ajax({
                url: "/web/api/rating/rate",
                method: "PUT",
                data: JSON.stringify({
                    pageId: $(starContainer).data("page-id"),
                    score: score
                }),
                contentType: "application/json",
                dataType: "json"
            }).then(function(response) {
                $(starContainer).data("rating", response.averageScore);
                restoreRating();
                stars.off("click mouseenter mouseleave");
                $(starContainer).find(".rating__total").text(response.totalScored);
                $(starContainer).find(".rating__average").text(response.averageScore);
                $(starContainer).removeClass("loading");
            }.bind(this));
        });
    });
});