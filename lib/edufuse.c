#define FUSE_USE_VERSION 31

#include "edufuse.h"
#include <fuse.h>
#include <string.h>
#include <errno.h>

static const char *filepath = "/file";
static const char *filename = "file";
static const char *filecontent = "I'm the content of the only file available there\n";

static int edufuse_register(int argc, char *argv[], struct fuse_operations *edufuse_operations) {
    // store a struct to allow us to redirect back to the implementation
    // likely done through copying their struct, then modifying and passing a modified version to fuse

    return fuse_main(argc, argv, edufuse_operations, NULL);
}

static int edufuse_getattr(const char *path, struct stat *stbuf) {
  memset(stbuf, 0, sizeof(struct stat));

  if (strcmp(path, "/") == 0) {
    stbuf->st_mode = S_IFDIR | 0755;
    stbuf->st_nlink = 2;
    return 0;
  }

  if (strcmp(path, filepath) == 0) {
    stbuf->st_mode = S_IFREG | 0777;
    stbuf->st_nlink = 1;
    stbuf->st_size = strlen(filecontent);
    return 0;
  }

  return -ENOENT;
}

static int edufuse_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
    off_t offset, struct fuse_file_info *fi) {
  (void) offset;
  (void) fi;

  filler(buf, ".", NULL, 0);
  filler(buf, "..", NULL, 0);

  filler(buf, filename, NULL, 0);

  return 0;
}

static int edufuse_open(const char *path, struct fuse_file_info *fi) {
  return 0;
}

static int edufuse_read(const char *path, char *buf, size_t size, off_t offset,
    struct fuse_file_info *fi) {

  if (strcmp(path, filepath) == 0) {
    size_t len = strlen(filecontent);
    if (offset >= len) {
      return 0;
    }

    if (offset + size > len) {
      memcpy(buf, filecontent + offset, len - offset);
      return len - offset;
    }

    memcpy(buf, filecontent + offset, size);
    return size;
  }

  return -ENOENT;
}

static struct fuse_operations edufuse_operations = {
  .getattr = edufuse_getattr,
  .open = edufuse_open,
  .read = edufuse_read,
  .readdir = edufuse_readdir,
};

int main(int argc, char *argv[])
{
  return fuse_main(argc, argv, &edufuse_operations, NULL);
}
