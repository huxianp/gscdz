/**
 * MathJax初始配置文件
 */
function initialMathJax(){
    MathJax.Hub.Config({
        tex2jax: {
            inlineMath: [["$","$"],["\\(","\\)"]]
        }
    });
};