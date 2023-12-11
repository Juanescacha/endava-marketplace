<script setup>
	defineProps({
		stock: {
			type: Number,
			required: true,
			validator(value) {
				return value > 0;
			},
		},
	});
	const emit = defineEmits(["quantitySelected"]);

	const handleOptionChange = $event => {
		const selectedQuantity =
			$event.target.options[$event.target.selectedIndex].value;
		emit("quantitySelected", Number(selectedQuantity));
	};
</script>

<template>
	<select
		class="py-1 pr-8"
		data-ui="select-qty"
		@input="handleOptionChange"
	>
		<option
			v-for="qty in stock"
			data-ui="qty-option"
			:key="`quantityOption${qty}`"
			:value="qty"
		>
			{{ `${qty} unit${qty === 1 ? "" : "s"}` }}
		</option>
	</select>
</template>
