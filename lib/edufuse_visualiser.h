#ifndef EDUFUSE_EDUFUSE_VISUALISER_H
#define EDUFUSE_EDUFUSE_VISUALISER_H

#include <fuse.h>

extern int init_visualiser();
extern void destroy_visualiser();
extern int send_data(char *str);
extern int send_log(char *syscall, char *file, char *fileInfo);

extern char *stringify_stat(struct stat *stbuf);
#endif //EDUFUSE_EDUFUSE_VISUALISER_H
