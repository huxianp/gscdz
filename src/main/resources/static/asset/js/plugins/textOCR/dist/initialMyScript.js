(function () {
    function initialize() {
        var result = document.getElementById('result');
        var inkPaper = document.getElementById('ink-paper');

        var trash = document.getElementById('trash');
        var undo = document.getElementById('undo');
        var redo = document.getElementById('redo');
        var languages = document.getElementById('languages');

        var options = {
            host: "webdemoapi.myscript.com", // Handle MyScript Cloud authentication keys (cloud.myscript.com)
            applicationKey: "1526f528-7d79-440f-91e8-0b4453419fe1", // MyScript Cloud application key - change the keys for production use
            hmacKey: "9165b016-e2eb-4354-92b0-4f860b8ab8d5", // MyScript Cloud HMAC key - change the keys for production use
            type: MyScript.RecognitionType.TEXT, // Recognition type
            protocol: MyScript.Protocol.WS,
            width: 800, // InkPaper width
            height: 600, // InkPaper height
            timeout: 25, // Recognition timeout
            strokecolor:"#000",
            textParameters: {
                language: 'zh_CN', // Set the recognition language (i.e.: 'en_US', 'fr_FR', ...)
                resultDetail: 'CHARACTER',
                textProperties: {
                    textCandidateListSize: 10
                }
            }
        };
        /**
         * As canvas are positioned absolute to overlay, we have to explicitly set the parent dimensions
         */
        inkPaper.style.height = options.height + 'px';
        inkPaper.style.width = options.width + 'px';
        /**
         * Init the ink paper
         * @param {Element} The DOM element to attach the ink paper
         * @param {Object} The recognition options
         * @param {Function} The result callback
         */
        var paper = new MyScript.InkPaper(inkPaper, options);
        paper.setResultCallback(function (data, error) {
            // Handle recognition result
            result.innerHTML = '';
            if (error) {
                result.innerHTML = error;
            }
            if (data) {
                if (data instanceof Array) { // Populate the language options

                    for (var i in data) {
                        languages.options[languages.options.length] = new Option(data[i], data[i]);
                        languages.disabled = false;
                        languages.value = "zh_CN";
                    }
                } else { // Display the selected text candidate
                    var array = data.getDocument().getTextSegment().candidates;
                    console.log(array)
                    for(var i=0;i<3;++i){
                        var con = document.createElement("span");
                        var txt = document.createTextNode(array[i].label);
                        con.appendChild(txt);
                        result.appendChild(con);
                    }
                    // result.innerHTML = data.getDocument().getTextSegment().getSelectedCandidate().getLabel();
                    $("#result span").click(function () {
                        $(this).css("border","2px solid #32ce32")
                        $(this).siblings().css("border","1px dashed #aaa");
                        sessionStorage.setItem("textRecongnizeResult", $(this).text());
                    })
                }
            }
        });
        paper.setChangeCallback(function (data) {
            if (data) {
                trash.disabled = (data.canUndo || data.canRedo) ? false : true;
                undo.disabled = (data.canUndo) ? false : true;
                redo.disabled = (data.canRedo) ? false : true;
            }
        });
        /**
         * Map the buttons
         */
        trash.addEventListener('click', function () {
            paper.clear();
        }, false);
        undo.addEventListener('click', function () {
            paper.undo();
        }, false);
        redo.addEventListener('click', function () {
            paper.redo();
        }, false);
        languages.addEventListener('change', function () {
            var parameters = paper.getTextParameters();
            parameters.setLanguage(languages.options[languages.selectedIndex].value);
            paper.setTextParameters(parameters);
        }, false);
        paper.getAvailableLanguages();
    }
    MyScript.InkPaper.event.addDomListener(window, 'load', initialize);
})();