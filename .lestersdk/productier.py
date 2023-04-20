import os
import string
import random


def main():
	iterates = 50
	
	all_product_names = []
	all_letters = list(string.ascii_uppercase)
	
	all_product_info = {
		"Cement" : (True, ["sack/s"]),
		"Tool" : (True, ["pc/s", "set/s"]),
		"Roofing" : (False, ["cm2", "ft2", "m2", "yrd2", "inch2"]),
		"Lumber" : (False, ["cm", "ft", "m", "yrd", "inch"]),
		"Insulation" : (True, ["pc/s", "set/s", "pair/s"]),
		"Electrical" : (False, ["pc/s", "pair/s", "set/s", "cm", "ft", "m", "yrd", "inch"]),
		"Plumbing" : (False, ["pc/s", "pair/s", "set/s", "cm", "ft", "m", "yrd", "inch"]),
		"Paint" : (False, ["pc/s", "pair/s", "set/s", "L", "mL", "gal", "cm3", "ft3", "m3", "yrd3", "inch3"])
	}
	
	all_product_categories = list(all_product_info.keys())
	
	const_c = ""
	cout = 0
	intout = 0
	lvl = 0
	
	same_prd = []
	
	file_ = open("inventory.txt", "w")
	
	for i, _ in enumerate(range(iterates)):
		print(i)
		if (i % 25) == 0:
			cout = 0
		
		if not (lvl < 2):
			lvl = 0
			const_c = ""
		
		if (i % 99) == 0:
			intout = 0
			const_c += all_letters[lvl]
			lvl += 1

		ran_c = random.choice(all_letters)
		ran_int = random.randint(1, 9)
		
		name = list(str(ran_int) + const_c + ran_c + all_letters[cout] + str(intout))
		random.shuffle(name)
		
		name = "PRD" + "".join(name)

		category = random.choice(all_product_categories)
		is_whole, unit_ = all_product_info[category]
		
		quantity = 0
		
		if is_whole:
			quantity = random.randint(88, 999)
		else:
			quantity = random.uniform(88.0, 999.0)
		
		unit = random.choice(unit_)
		price = random.uniform(15.99, 150.99)
		
		if i == iterates - 1:
			file_.write(f"{category} | {name} | {unit} | {quantity} | {price}")
		else:
			file_.write(f"{category} | {name} | {unit} | {quantity} | {price}\n")

		cout += 1
		intout += 1
	
	file_.close()


if __name__ == "__main__":
	main()
	print("wtf")
