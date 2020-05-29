# compiler

compiler1为词法分析器

compiler2为LL1语法分析器
测试数据暂定为已消除左递归。
E->TG
G->+TG
G->-TG
G->ε
T->FS
S->*FS
S->/FS
S->ε
F->(E)
F->i

i+i*i#

compiler3为LR1语法分析器