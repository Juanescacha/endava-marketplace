<template>
	<div class="h-64 w-52">
		<a
			class="group flex flex-col overflow-hidden rounded-xl border bg-white shadow-sm transition hover:shadow-lg"
			href="#"
		>
			<div
				class="relative overflow-hidden rounded-t-xl pt-[80%] sm:pt-[80%] lg:pt-[80%]"
			>
				<img
					class="absolute left-0 top-0 h-full w-full rounded-t-xl object-contain transition-transform duration-500 ease-in-out group-hover:scale-110"
					:src="cardInfo.media"
					alt="Image Description"
				/>
			</div>
			<div class="flex flex-col gap-2 p-4 md:p-5">
				<span class="text-lg font-bold text-gray-800">{{
					cardInfo.price
				}}</span>
				<p class="truncate text-xs text-gray-800">
					{{ cardInfo.name }}
				</p>
			</div>
		</a>
	</div>
</template>
<script setup>
	defineProps({
		cardInfo: {
			price: String,
			name: String,
			media: String,
		},
	});
</script>
