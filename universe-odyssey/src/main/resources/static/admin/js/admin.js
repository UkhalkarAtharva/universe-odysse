// admin common JS
async function postJson(url, payload){
  const res=await fetch(url,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(payload)});
  return res;
}
async function getJson(url){
  const res=await fetch(url,{credentials:'same-origin'});
  return res;
}
function redirectToLogin(){ window.location.href='/admin/pages/login.html'; }
