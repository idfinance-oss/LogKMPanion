Pod::Spec.new do |spec|
    spec.name                     = 'debug_view_no_impl'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://idfinance.com/'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Debug View No Impl'
    spec.vendored_frameworks      = 'build/cocoapods/framework/DebugViewNoImpl.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '12.0'
                
                
    if !Dir.exist?('build/cocoapods/framework/DebugViewNoImpl.framework') || Dir.empty?('build/cocoapods/framework/DebugViewNoImpl.framework')
        raise "

        Kotlin framework 'DebugViewNoImpl' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :debug-view-no-impl:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':debug-view-no-impl',
        'PRODUCT_MODULE_NAME' => 'DebugViewNoImpl',
    }
                
    spec.script_phases = [
        {
            :name => 'Build debug_view_no_impl',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end