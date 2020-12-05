var STATUS_CODE = {
  0 : "成功",
  1 : "このIDは既に登録されています",
  2 : "IDのフォーマットが合いません",
  3 : "ID又はパスワードが一致しません", //idがおかしい

  10 : "ID又はパスワードが一致しません", //passがおかしい
  11 : "パスワードのフォーマットが間違っています",

  20 : "入力されたデータのフォーマットが一致しません",
  
  30 : "USERデータが見つかりません ログインし直してください"
};

function getErrorMessage(code) {
  let message = STATUS_CODE[code];
  if(message == null) return "サーバーエラー errorcode:"+code;
  else return message;
}