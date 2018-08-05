const gulp = require("gulp"),
    resources = require("gulp-resources"),
    RevAll = require("gulp-rev-all"),
    useref = require("gulp-useref"),
    filter = require('gulp-filter'),
    cssref = require("gulp-css-useref"),
    gulpif = require("gulp-if"),
    minifyJs = require("gulp-uglify"),
    minifyCss = require("gulp-csso");

const output = "../dist/web";

gulp.task("clean", function() {
    const del = require("del");
    return del(output, {force: true});
});

gulp.task("release", ["clean"], function() {
    const excludeNodeModules = filter(["**/*", "!node_modules/**/*"]);
    return gulp.src(["favicon.ico",
        "robots.txt",
        "template/**/*.html",
        "component/**/*.html",
        "theme/**/*.html",
        "static/img/**/*"])
        .pipe(resources({skipNotExistingFiles: true}))
        .pipe(cssref({
            base: '../web/static'
        }))
        .pipe(useref())
        .pipe(RevAll.revision({
            hashLength: 4,
            dontRenameFile: [/^\/favicon.ico$/g, /^\/robots.txt$/g, ".html"]
        }))
        .pipe(excludeNodeModules)
        .pipe(gulpif("*.js", minifyJs()))
        .on('error', function(err) {
            console.log(err.toString());
        })
        .pipe(gulpif("*.css", minifyCss()))
        .pipe(gulp.dest(output));
});
