<script setup></script>
<template>
	<div
		class="grid h-[max(4rem,auto)] grid-cols-7 gap-3 px-7 py-3 sm:grid-cols-2"
	>
		<div class="col-span-3 flex items-center gap-3 sm:col-span-1">
			<slot name="left-side"></slot>
		</div>
		<div
			class="col-span-4 flex items-center justify-between gap-4 sm:col-span-1"
		>
			<slot name="right-side"></slot>
		</div>
	</div>
</template>
