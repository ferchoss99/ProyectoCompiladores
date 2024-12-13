.data
a: .word  0
b: .word  0
c: .word  0
d: .word  0
w: .word  0
g: .word  0
x: .word  0
h: .word  0
y: .word  0
z: .word  0
 
 
 
.text
main:
 
move $x, $2.0
move $y, $12.0
div $t0, $x, $y
move $z, $t0
mul $t1, $x, $y
sub $t2, $z, $t1
add $t3, $z, $t2
move $w, $t3
slt $t4, $d, $a
and $t5, 1, $t4
move $h, $t5
j L2
L2:
move $x, $7.0
j L4
move $y, $4.0
