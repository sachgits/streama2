describe("streama module", function() {
    var scope;

    beforeEach(angular.mock.module("streama", function() {
    }));

    beforeEach(angular.mock.inject(function($rootScope) {
        scope = $rootScope.$new();
    }));

    describe("KbSubscription-ctrlController", function() {

        var ctrl;

        beforeEach(angular.mock.inject(function($controller) {
            ctrl = $controller("KbSubscription-ctrlController", {});
        }));

        it("should be tested", function() {
            expect(true).toEqual(false);
        });

    });

});
