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
        // dmg: {
        //   contents: [
        //     {
        //       x: 855,
        //       y: 600,
        //       type: "link",
        //       path: "/Applications"
        //     },
        //     {
        //       x: 130,
        //       y: 150,
        //       type: "file"
        //     }
        //   ]
        // },
        mac: {
          target: "pkg",
          icon: "build/icons/icon.icns"
        },
        win: {
          icon: "build/icons/icon.ico"
        },
        linux: {
          icon: "build/icons"
        }
      },
      }
    }
};
