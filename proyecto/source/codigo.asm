.data
a: .word  0
b: .word  0
c: .word  0
d: .word  0
w: .word  0
y: .word  0
z: .word  0
 
 
 
.text
main:
 
move $x, $4.0
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
beq $5.0, $b, $L4
j L6
L4:
move $x, $7.0
j L8
L6:
j L10
move $y, $4.0
L10:
slt $t0, $5.0, $b
bne $t0, $zero, L13
j L15
L13:
j L17
move $y, $43.0
j L19
L17:
move $z, $3.0
j L21
L15:
move $z, $2.0
move $h, $2.0
move $y, $2.0
