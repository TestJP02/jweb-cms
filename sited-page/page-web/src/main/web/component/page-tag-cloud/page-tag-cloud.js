$(function () {
    $.fn.tagcloud.defaults = {
        size: {start: 14, end: 18, unit: 'pt'},
        color: {start: '#666', end: '#333'}
    };

    $('.page-tag-cloud__link').tagcloud();
});