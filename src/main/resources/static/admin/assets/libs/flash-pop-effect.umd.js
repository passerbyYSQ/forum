!function(n,e){"object"==typeof exports&&"undefined"!=typeof module?module.exports=e():"function"==typeof define&&define.amd?define(e):(n="undefined"!=typeof globalThis?globalThis:n||self).flash=e()}(this,(function(){"use strict";return function(n){if(function(){const n=document.createElement("style");n.textContent=".__ani {\n  animation-name: __flash-pop;\n  animation-duration: 0.5s;\n}\n\n@keyframes __flash-pop{\n  from{\n    opacity: 1;\n  }\n\n  to{\n    opacity: 0;\n    transform: scale(1.1, 1.1)\n  }\n}",document.body.appendChild(n)}(),"string"==typeof n){if(!(n=document.querySelector(n)))throw new Error("Can't find dom");!function(n){n.addEventListener("click",(e=>{const{top:t,left:o}=n.getBoundingClientRect(),i=n.cloneNode();i.classList.add("__ani"),i.style.position="absolute",i.style.top=`${t}px`,i.style.left=`${o}px`,i.style.zIndex=9999,i.style.pointerEvents="none",document.body.appendChild(i),setTimeout((()=>{i.remove()}),1e3)}))}(n)}}}));
