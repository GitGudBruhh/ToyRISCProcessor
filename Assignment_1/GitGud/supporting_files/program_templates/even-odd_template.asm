	.data
a:
	15
	.text
main:
	load %x0, $a, %x3
	divi %x3, 2, $x4
	beq %x0, %x31, $even
	bne %x0, %x31, $odd
even:
	subi %x0, 1, %x10
	end
odd:
	addi %x0, 1, %x10
	end
