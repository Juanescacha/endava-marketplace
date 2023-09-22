<template>
	<router-link
		class="h-64 w-52"
		:to="'/listings/' + cardInfo.id"
	>
		<div
			class="group flex flex-col overflow-hidden rounded-xl border bg-white shadow-sm transition hover:shadow-lg"
			href="#"
		>
			<div
				class="relative overflow-hidden rounded-t-xl pt-[80%] sm:pt-[80%] lg:pt-[80%]"
			>
				<img
					class="absolute left-0 top-0 h-full w-full rounded-t-xl object-cover transition-transform duration-500 ease-in-out group-hover:scale-110"
					:src="imageSrc"
					alt="Image Description"
				/>
			</div>
			<div class="flex flex-col gap-2 p-4 md:p-5">
				<span class="text-lg font-bold text-gray-800">{{ price }}</span>
				<p class="truncate text-xs text-gray-800">
					{{ cardInfo.name }}
				</p>
			</div>
		</div>
	</router-link>
</template>
<script setup>
	import { ref, onBeforeMount } from "vue";
	import { makeGetRequest } from "../utils/axios";

	const props = defineProps({
		cardInfo: {
			id: Number,
			price: String,
			name: String,
			media: String,
		},
	});

	const url = `${import.meta.env.VITE_API_URL}/api/listings/get/images/${
		props.cardInfo.id
	}`;

	const imageSrc = ref("#");

	onBeforeMount(async () => {
		const response = await makeGetRequest(url);
		if (response.error) {
			// error
		} else {
			imageSrc.value = findThumb(response.data);
			if (props.cardInfo.id === 56) {
				imageSrc.value = response.data[0];
			}
		}
	});

	const findThumb = array => {
		for (let i = 0; i < array.length; i++) {
			if (array[i].includes("thumb")) return array[i];
		}
	};

	const moneyFormat = value => {
		const numeroFormateado = parseInt(value).toLocaleString("es-ES", {
			style: "currency",
			currency: "COP",
			minimumFractionDigits: 0,
			maximumFractionDigits: 0,
		});
		return `$${numeroFormateado.trim().slice(0, -3)}`;
	};

	const price = ref(moneyFormat(props.cardInfo.price));
</script>
