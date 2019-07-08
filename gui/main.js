const {app, Menu, BrowserWindow} = require('electron');
const path = require('path');
const net = require('net');
const port = 8080;
const host = '127.0.0.1';

let mainWindow;

//todo move all of this logic out of main, its only here for socket communication testing purposes
var server = net.createServer(function(socket) {
  socket.on('data', function(data){
    let str = data.toString('utf8');
    console.log(str);
    try {
      let json = JSON.parse(str);
      console.log(json);
    } catch (e) {
      console.log('error str: ' + str);
    }

  });
  socket.on('error', function(err) {
    console.log(err)
  })
});

server.listen(port, host);

function createWindow () {
  // Create the browser window.
  mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  });

  // and load the index.html of the app.
  mainWindow.loadFile('index.html');

  // Open the DevTools.
  // mainWindow.webContents.openDevTools()

  // Emitted when the window is closed.
  mainWindow.on('closed', function () {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    mainWindow = null
  })
}

Menu.setApplicationMenu(null);

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow);

// Quit when all windows are closed.
app.on('window-all-closed', function () {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') app.quit()
});

app.on('activate', function () {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (mainWindow === null) createWindow()
});

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
