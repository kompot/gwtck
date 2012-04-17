CKEDITOR.plugins.add('imagepaste',
{
  init:function (editor) {
    editor.on('paste', function (e) {
      var html = e.data.html;
      if (!html)
        return;
      e.data.html = html.replace(/src="data:image\/[a-z]+;base64,[^\s]*?"/g, function (img) {
        return img.replace(/"data:image\/[a-z]+;base64,[^\s]*?"/, "base64-image-is-now-allowed.jpg");
      });
    });
  }
});
