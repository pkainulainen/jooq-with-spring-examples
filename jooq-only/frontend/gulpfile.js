'use strict';

var targetPath = './_public/frontend/';

var vendorCodeFiles = [
    './bower_components/console-polyfill/index.js',
    './bower_components/lodash/dist/lodash.min.js',
    './bower_components/jquery/dist/jquery.min.js',
    './bower_components/momentjs/min/moment.min.js',
    './bower_components/angular/angular.min.js',
    './bower_components/angular-i18n/angular-locale_fi-fi.js',
    './bower_components/angular-cookies/angular-cookies.min.js',
    './bower_components/angular-moment/angular-moment.min.js',
    './bower_components/angular-resource/angular-resource.min.js',
    './bower_components/angular-sanitize/angular-sanitize.min.js',
    './bower_components/angular-translate/angular-translate.min.js',
    './bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js',
    './bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.min.js',
    './bower_components/angular-translate-storage-local/angular-translate-storage-local.min.js',
    './bower_components/angular-translate-handler-log/angular-translate-handler-log.min.js',
    './bower_components/angular-ui-router/release/angular-ui-router.min.js',
    './bower_components/angular-ui-utils/ui-utils.min.js',
    './bower_components/angular-ui-utils/ui-utils-ieshiv.min.js',
    './bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
    './bower_components/angular-growl/build/angular-growl.min.js',
    './bower_components/angular-utils-pagination/dirPagination.js'

];

var styleFiles = [
    './app/styles/app.less'
];

var appCodeFiles = "./app/**/*.js";
var codeFiles = vendorCodeFiles.concat(appCodeFiles);
var partialFiles = [
    './app/assets/partials/**/*.html'
];
var staticFiles = './app/assets/**';

var pkg = require('./package.json');
var banner = ['/**', ' * <%= pkg.name %> - <%= pkg.description %>',
    ' * @version v<%= pkg.version %>', ' */', ''].join('\n');

// Include gulp
var gulp = require("gulp");

// Include plugins
var jshint = require('gulp-jshint');
var header = require('gulp-header');
var footer = require('gulp-footer');
var ngHtml2Js = require("gulp-ng-html2js");
var minifyHtml = require("gulp-minify-html");
var concat = require("gulp-concat");
var uglify = require("gulp-uglify");
var rename = require('gulp-rename');
var less = require('gulp-less');
var minifyCSS = require('gulp-minify-css');
var path = require('path');
var size = require('gulp-size');

// Browser code live-reload
var lr = require('tiny-lr');
var livereload = require('gulp-livereload');
var server = lr();

// Lint Task
gulp.task('lint', function () {
    return gulp.src(appCodeFiles)
        .pipe(jshint('.jshintrc'))
        .pipe(jshint.reporter('default'));
});

// Concatenate & Minify application JS
gulp.task('scripts', function () {
    return gulp.src(appCodeFiles)
        .pipe(concat('app.js'))
        .pipe(gulp.dest(targetPath + 'js'))
        .pipe(rename('app.min.js'))
        .pipe(header(banner, { pkg: pkg }))
        .pipe(uglify({outSourceMap: true}))
        .pipe(footer('//@ sourceMappingURL=/frontend/js/app.min.js.map'))
        .pipe(size({showFiles: true}))
        .pipe(gulp.dest(targetPath + 'js'))
        .pipe(livereload(server));
});

// Concatenate & Minify vendor JS
gulp.task('vendor', function () {
    return gulp.src(vendorCodeFiles)
        .pipe(concat('vendor.min.js'))
        .pipe(size({showFiles: true}))
        .pipe(gulp.dest(targetPath + 'js'))
        .pipe(livereload(server));
});

// Compile Angular templates
gulp.task('partials', function () {
    return gulp.src(partialFiles)
        .pipe(gulp.dest(targetPath + 'partials'))
        .pipe(minifyHtml({
            empty: true,
            spare: true,
            quotes: true
        }))
        .pipe(ngHtml2Js({
            moduleName: 'app.partials',
            prefix: 'frontend/partials/'
        }))
        .pipe(concat('partials.js'))
        .pipe(size({showFiles: true}))
        .pipe(gulp.dest(targetPath + 'js'))
        .pipe(livereload(server));
});

// Compile stylesheet to CSS and minify
gulp.task('less', function () {
    gulp.src(styleFiles)
        .pipe(less({paths: [ path.join('.', 'app', 'styles') ]}))
        .pipe(concat('app.css'))
        .pipe(minifyCSS())
        .pipe(header(banner, { pkg: pkg }))
        .pipe(size({showFiles: true}))
        .pipe(gulp.dest(targetPath + 'css'))
        .pipe(livereload(server));
});

// Copy static assets: images, fonts, etc.
gulp.task('static', function() {
    gulp.src(staticFiles, {base: './app/assets/'})
        .pipe(gulp.dest(targetPath + ''))
        .pipe(livereload(server));
});

// Watch for changes and re-execute build automatically
gulp.task('watch', function () {
    server.listen(35729, function (err) {
        if (err) return console.log(err);

        gulp.watch(partialFiles, ['partials']);
        gulp.watch(codeFiles, ['scripts']);
        gulp.watch(vendorCodeFiles, ['vendor']);
        gulp.watch(styleFiles.concat('./app/styles/**/*'), ['less']);
        gulp.watch(staticFiles, ['static']);
    });
});

gulp.task('build', ['less', 'static', 'partials', 'scripts', 'vendor']);

gulp.task('default', ['lint', 'build', 'watch']);
