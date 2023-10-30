const headerNode = document.getElementsByClassName('.header')[0];
const media_web  = window.matchMedia("screen and (min-width: 1201px)");
const media_tablet  = window.matchMedia("screen and (max-width: 568px)");
const media_only_tablet  = window.matchMedia("screen and (max-width: 1200px) and (min-width:568px)");
const media_mobile  = window.matchMedia("screen and (max-width: 568px)");
window.onload = function () {
  
}

if(media_web.matches){
  alert('web')
}

/* only tablet */
if(media_only_tablet.matches){
  alert('tab')
}
if(media_mobile.matches){
  alert('mo')
}
/*
media_web.addListener(function(e){
  if(e.matches){
    alert('web')
  }

})
media_tablet.addListener(function(e){
  if(e.matches){
    alert('tablet')
  }
})
media_mobile.addListener(function(e){

   if(e.matches){
    alert('mobile')
  }
})
*/