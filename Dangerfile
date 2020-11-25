
# Check if changelog.yaml was updated when pushing to beta.

if github.branch_for_base == "beta"
    warn "Please update changelog.yaml when pushing to beta branch!" unless git.modified_files.include? "changelog.yaml"
end
