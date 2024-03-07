	.data
n:
	15
	.text
main:
	add %x0, %x0, %x5
	addi %x0, 1, %x6
	subi %x0, 1, %x3
	store %x5, 65536, %x3
	subi %x3, 1, %x3
	store %x6, 65536, %x3
	add %x0, %x0, %x4
	load %x0, $n, %x9
	subi %x9, 3, %x9
	blt %x9, 0, terminate
loop:
	add %x5, %x6, %x4
	subi %x3, 1, %x3
	store %x4, 65536, %x3
	add %x0, %x6, %x5
	add %x0, %x4, %x6
	subi %x9, 1, %x9
	blt %x9, 0, terminate
	jmp loop
terminate:
	end