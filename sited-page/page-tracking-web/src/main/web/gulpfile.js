const gulp = require("gulp"),
    resources = require("gulp-resources"),
    RevAll = require("gulp-rev-all"),
    gulpif = require("gulp-if"),
    minifyJs = require("gulp-uglify"),
    minifyCss = require("gulp-csso");

const output = "../dist/web";

gulp.task("clean", function() {
    const del = require("del");
    return del(output, {force: true});
});

gulp.task("release", ["clean"], function() {
    return gulp.src(["favicon.ico",
        "robot.txt",
        "template/**/*.html",
        "component/**/*.html",
        "static/**/*",
        "node_modules/font-awesome/fonts/*"], {base: "."})
        .pipe(resources())
        .pipe(RevAll.revision({
            hashLength: 4,
            dontRenameFile: [/^\/favicon.ico$/g, /^\/robot.txt$/g, ".html"]
        }))
        .pipe(gulpif("*.js", minifyJs()))
        .pipe(gulpif("*.css", minifyCss()))
        .pipe(gulp.dest(output));
});