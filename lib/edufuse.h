#include <fuse.h>

#ifndef EDUFUSE_EDUFUSE_H
#define EDUFUSE_EDUFUSE_H

    // feel like we shouldn't be defining 'struct' in prefix??
    extern int edufuse_register(int argc, char *argv[], struct fuse_operations *, int size);

#endif //EDUFUSE_EDUFUSE_H
