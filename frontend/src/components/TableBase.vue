<script setup>
	defineProps({
		columns: {
			type: Array,
			required: true,
		},
	});
</script>

<template>
	<table class="w-full">
		<thead class="border-b-4 border-gray-900">
			<tr>
				<th
					v-for="column in columns"
					:key="column"
					class="text-left"
				>
					{{ column }}
				</th>
			</tr>
		</thead>
		<tbody>
			<slot></slot>
		</tbody>
	</table>
</template>
