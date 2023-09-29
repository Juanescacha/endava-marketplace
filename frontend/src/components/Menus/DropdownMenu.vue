<script setup>
	import { Menu, MenuButton, MenuItems } from "@headlessui/vue";
</script>

<template>
	<div>
		<Menu
			as="div"
			class="relative inline-block"
		>
			<div>
				<MenuButton
					class="inline-flex justify-center px-2 py-2 focus:outline-none"
				>
					<slot name="menu-button"></slot>
				</MenuButton>
			</div>

			<transition
				enter-active-class="transition duration-100 ease-out"
				enter-from-class="transform scale-95 opacity-0"
				enter-to-class="transform scale-100 opacity-100"
				leave-active-class="transition duration-75 ease-in"
				leave-from-class="transform scale-100 opacity-100"
				leave-to-class="transform scale-95 opacity-0"
			>
				<MenuItems
					class="absolute right-0 z-10 w-56 origin-top-right divide-y divide-gray-100 rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
				>
					<slot name="menu-items"></slot>
				</MenuItems>
			</transition>
		</Menu>
	</div>
</template>
