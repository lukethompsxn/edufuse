#ifndef EDUFUSE_EDUFUSE_VISUALISER_H
#define EDUFUSE_EDUFUSE_VISUALISER_H

#include <fuse.h>

extern int init_visualiser(char *mount_point);
extern void destroy_visualiser();
extern int send_data(char *str);
extern int send_mount_point(char *mount_point);
extern int send_log(char *syscall, char *file, char *fileInfo);
extern int send_amount_read_write(char *syscall, int *amount);

extern char *stringify_stat(struct stat *stbuf);
extern char *stringify_fusefileinfo(struct fuse_file_info *fi);
extern char *stringify_fusefileinfo_with_buf_size_off(struct fuse_file_info *fi, char *buf, size_t size, off_t off);
extern char *stringify_fusefileinfo_with_datasync(struct fuse_file_info *fi, int datasync);
extern char *stringify_fusefileinfo_with_mode(struct fuse_file_info *fi, int mode);
extern char *stringify_fusefileinfo_with_flock_cmd(struct fuse_file_info *fi, struct flock *lock, int cmd);
extern char *stringify_statvfs(struct statvfs *buf);
extern char *stringify_fuseconninfo(struct fuse_conn_info *conn);
extern char *stringify_tv(struct timespec tv[2]);

#endif //EDUFUSE_EDUFUSE_VISUALISER_H
