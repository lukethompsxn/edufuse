'use strict';

import { app, protocol, BrowserWindow, ipcMain } from 'electron';
import { createProtocol, installVueDevtools } from 'vue-cli-plugin-electron-builder/lib';
import walkdir from 'walkdir';
// import chokidar from 'chokidar';
const isDev = process.env.NODE_ENV !== 'production';

// Global reference so object is not garbage collected.
let window;

protocol.registerSchemesAsPrivileged([{ scheme: 'app', privileges: { secure: true, standard: true } }]);

function createWindow() {
  window = new BrowserWindow({
    width: 850,
    height: 600,
    webPreferences: {
      nodeIntegration: true
    }
  });

  if (process.env.WEBPACK_DEV_SERVER_URL) {
    window.loadURL(process.env.WEBPACK_DEV_SERVER_URL);
    if (!process.env.IS_TEST) window.webContents.openDevTools();
  } else {
    createProtocol('app');
    window.loadURL('app://./index.html');
  }

  scanDirectory();
}

// Quit when all windows are closed.
app.on('window-all-closed', () => {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (window === null) {
    createWindow();
  }
});

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', async () => {
  if (isDev && !process.env.IS_TEST) {
    // Install Vue Devtools
    try {
      await installVueDevtools();
    } catch (e) {
      console.error('Vue Devtools failed to install:', e.toString());
    }
  }
  createWindow();
});

// Exit cleanly on request from parent process in development mode.
if (isDev) {
  if (process.platform === 'win32') {
    process.on('message', data => {
      if (data === 'graceful-exit') {
        destroyWatcher();
        app.quit();
      }
    });
  } else {
    process.on('SIGTERM', () => {
      destroyWatcher();
      app.quit();
    });
  }
}

let dir = '/tmp/test/';
if (dir.lastIndexOf('/') === dir.length - 1) {
  dir = dir.substring(0, dir.lastIndexOf('/'));
}
const index = dir.lastIndexOf('/');

function scanDirectory() {
  console.log('scan called');
  window.webContents.send('clear-nodes');
  walkdir(dir, {})
      .on('file', (fn, stat) => {
        window.webContents.send('file', fn.slice(index), stat);
      })
      .on('directory', (fn, stat) => {
        window.webContents.send('directory', fn.slice(index), stat);
      })
      .on('error', (fn, err) => {
        console.error(`!!!! ${fn} ${err}`);
      });
}

ipcMain.on('rescan-directory', () => {
  scanDirectory();
});

var chokidar = require('chokidar');

var watcher = chokidar.watch(dir, {ignored: /^\./, persistent: true});

watcher
    .on('add', function(path) {console.log('File', path, 'has been added');})
    .on('change', function(path) {console.log('File', path, 'has been changed');})
    .on('unlink', function(path) {console.log('File', path, 'has been removed');})
    .on('error', function(error) {console.error('Error happened', error);});


function destroyWatcher() {
  watcher.unwatch(dir);
  watcher.close();
}
//
// let watcher = chokidar.watch(dir, {ignored: /^\./, persistent: true, awaitWriteFinish: true});
// watcher
//     .on('add', function() {scanDirectory();})
//     .on('change', function() {scanDirectory();})
//     .on('unlink', function() {scanDirectory();})
//     .on('error', function() {scanDirectory();}
//     );