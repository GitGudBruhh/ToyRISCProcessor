	.data
a:
	110
	.text
main:
	load %x0, $a, %x4
	jmp loop
loop:
	subi %x4, 2, %x4
	beq %x0, %x4, even
	bgt %x0, %x4, odd
	jmp loop
even:
	subi %x0, 1, %x10
	end
odd:
	addi %x0, 1, %x10
	end
