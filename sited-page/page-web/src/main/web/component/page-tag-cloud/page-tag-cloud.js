$(function() {
    $.fn.tagcloud.defaults = {
        size: {start: 14, end: 18, unit: 'pt'}
    };

    $('.page-tag-cloud__link').tagcloud();
});