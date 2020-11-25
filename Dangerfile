# Check if changelog.yaml was updated when pushing to beta.
if github.branch_for_base == "beta"
    failure "I see you haven't updated changelog.yaml. Hmm..." unless git.modified_files.include? "changelog.yaml"
end

# Check if we are merging from beta to master
if github.branch_for_base == "master"
    failure "We, at this great company merge to master only from beta branch." unless github.branch_for_head == "beta"
end
