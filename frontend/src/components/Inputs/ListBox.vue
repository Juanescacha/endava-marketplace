<script setup>
	import { ref } from "vue";
	import {
		Listbox,
		ListboxLabel,
		ListboxButton,
		ListboxOptions,
		ListboxOption,
	} from "@headlessui/vue";
	import { CheckIcon, ChevronUpDownIcon } from "@heroicons/vue/20/solid";

	const options = [
		{ name: "Categories" },
		{ name: "Clothes" },
		{ name: "Technology" },
		{ name: "Vehicles" },
	];
	const selectedOption = ref(options[0]);
</script>

<template>
	<Listbox
		v-model="selectedOption"
		class="w-52 shadow-sm"
	>
		<div class="relative">
			<ListboxButton
				class="relative w-full cursor-default rounded-lg border border-gray-200 bg-white py-2 pl-3 pr-10 text-left text-gray-500 focus:outline-none focus-visible:border-indigo-500 focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75 focus-visible:ring-offset-2 focus-visible:ring-offset-orange-300 sm:text-sm"
			>
				<span class="block truncate">{{ selectedOption.name }}</span>
				<span
					class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2"
				>
					<ChevronUpDownIcon
						class="h-5 w-5 text-gray-400"
						aria-hidden="true"
					/>
				</span>
			</ListboxButton>

			<transition
				leave-active-class="transition duration-100 ease-in"
				leave-from-class="opacity-100"
				leave-to-class="opacity-0"
			>
				<ListboxOptions
					class="absolute mt-1 max-h-60 w-full overflow-auto rounded-md bg-white text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"
				>
					<ListboxOption
						v-slot="{ active, selected }"
						v-for="option in options"
						:key="option.name"
						:value="option"
						as="template"
					>
						<li
							:class="[
								active
									? 'bg-orange-200 text-gray-900'
									: 'text-gray-500',
								'relative cursor-default select-none py-2 pl-10 pr-4',
							]"
						>
							<span
								:class="[
									selected
										? 'font-medium text-black'
										: 'font-normal',
									'block truncate',
								]"
								>{{ option.name }}</span
							>
							<span
								v-if="selected"
								class="absolute inset-y-0 left-0 flex items-center pl-3 text-orange-500"
							>
								<CheckIcon
									class="h-5 w-5"
									aria-hidden="true"
								/>
							</span>
						</li>
					</ListboxOption>
				</ListboxOptions>
			</transition>
		</div>
	</Listbox>
	<!-- <select
		id="categories"
		class="hidden w-52 rounded-md border-gray-200 py-2 pl-6 pr-11 text-sm font-light shadow-sm focus:z-10 focus:border-gray-200 focus:ring-gray-200 sm:block"
		:class="{
			'text-gray-500': categoryInput === '',
			'bg-white/20': categoryInput === '',
			'text-black': categoryInput !== '',
			'bg-white': categoryInput !== '',
		}"
		v-model="categoryInput"
	>
		<option
			value=""
			disabled
			selected
			hidden
		>
			Categories
		</option>
		<option value="1">Clothes</option>
		<option value="2">Technology</option>
		<option value="3">Vehicles</option>
	</select> -->
</template>
