	.data
a:
	17
	.text
main:
	load %x0, $a, %x7
	addi %x0, 2, %x3
	beq %x7, 2, Prime
loop:
	div %x7, %x3, %x4
	mul %x3, %x4, %x5
	sub %x7, %x5, %x6
	beq %x6, 0, notPrime
	addi %x3, 1, %x3
	blt %x3, %x7, Prime
	jmp loop
Prime:
	addi %x0, 1, %x10
	end
notPrime:
	subi %x0, 1, %x10
	end