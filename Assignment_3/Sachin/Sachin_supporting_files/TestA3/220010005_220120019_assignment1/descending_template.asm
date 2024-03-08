	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x3
	add %x0, %x0, %x4
	subi %x3, 1, %x3
loop:
	beq %x4, %x3, loopexit
	sub %x3, %x4, %x9
	addi %x4, 1, %x4
	add %x0, %x0, %x5
innerloop:
	beq %x5, %x9, loop
	addi %x5, 1, %x6
	load %x5, $a, %x7
	load %x6, $a, %x8
	add %x5, %x0, %x28
	addi %x5, 1, %x5
	bgt %x8, %x7, swap
	jmp innerloop
swap:
	store %x8, $a, %x28
	store %x7, $a, %x6
	jmp innerloop
loopexit:
	end