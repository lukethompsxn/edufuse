module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? `${process.cwd()}/dist/` : '/',
  pluginOptions: {
    electronBuilder: {
      builderOptions: {
        productName: "eduFUSE",
        appId: "com.github.lukethompsxn.edufuse",
        directories: {
          output: "build"
        },
        mac: {
          icon: "build/icons/icon.icns",
        },
        win: {
          icon: "build/icons/icon.ico",
        },
        linux: {
          icon: "build/icons/icon.icns",
        }
      }
    }
  }
};
