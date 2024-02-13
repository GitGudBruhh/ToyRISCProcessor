	.data
a:
	121
	.text
main:
	load %x0, $a, %x7
	add %x0, %x0, %x3
	add %x0, %x7, %x11
	addi %x0, 10, %x4
loop:
	add %x0, %x7, %x13
	div %x7, %x4, %x7
	mul %x7, %x4, %x8
	sub %x13, %x8, %x9
	mul %x3, %x4, %x10
	add %x10, %x9, %x3
	beq %x3, %x11, palindrome
	beq %x7, 0, condition
	jmp loop
palindrome:
	addi %x0, 1, %x10
	end
condition:
	subi %x0, 1, %x10
	end