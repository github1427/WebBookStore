function _change() {
	$("#vCode").attr("src", "/goods/verifyCode?" + new Date().getTime());
}